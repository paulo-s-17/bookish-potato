package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals; 

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Withdrawn;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferMethodCancelTest {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "António";

	private Sibs sibs;
	private Bank sourceBank;
	private Bank targetBank;
	private Client sourceClient;
	private Client targetClient;
	private Services services;
	
	@Before
	public void setUp() throws BankException, AccountException, ClientException {
		this.services = new Services();
		this.sibs = new Sibs(100, services);
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourceClient = new Client(this.sourceBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 33);
		this.targetClient = new Client(this.targetBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 22);
	}
	
	@Test
	public void successCanceled() throws BankException, AccountException, SibsException, OperationException, ClientException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);
		this.sibs.transfer(sourceIban, targetIban, 100);
		this.sibs.cancelOperation(0);
		assertEquals(1000, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, this.services.getAccountByIban(targetIban).getBalance());
		assertEquals(1, this.sibs.getNumberOfOperations());
		assertEquals(100, this.sibs.getTotalValueOfOperations());
		assertEquals(100, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
	}
	
	@Test
	public void mockTransferTestCancelDeposited() throws  AccountException, SibsException, OperationException {
		Services mockServices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockServices);
		when(mockServices.accountValidation("source", "target")).thenReturn(true);
		when(mockServices.equalBankValidation("source", "target")).thenReturn(true);
		sibs.transfer("source", "target", 100);
		Operation operation =  sibs.getOperation(0);
		sibs.processOperation();
		((TransferOperation) operation).setState(new Deposited());
		sibs.cancelOperation(0);
		verify(mockServices).withdraw("source", 100);
		verify(mockServices).deposit("target", 100);
		verify(mockServices).withdraw("target", 100);
		verify(mockServices).deposit("source", 100);
		assertEquals(sibs.getNumberOfOperations(), 1);
	}
	
	@Test
	public void mockTransferTestCancelWithdrawn() throws  AccountException, SibsException, OperationException {
		Services mockServices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockServices);
		when(mockServices.accountValidation("source", "target")).thenReturn(true);
		when(mockServices.equalBankValidation("source", "target")).thenReturn(true);
		sibs.transfer("source", "target", 100);
		Operation operation =  sibs.getOperation(0);
		sibs.processOperation();
		((TransferOperation) operation).setState(new Withdrawn());
		sibs.cancelOperation(0);
		verify(mockServices).withdraw("source", 100);
		verify(mockServices).deposit("target", 100);
		verify(mockServices).deposit("source", 100);
		assertEquals(sibs.getNumberOfOperations(), 1);
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
		this.sibs = null;
		this.sourceClient = null;
		this.targetClient = null;
		this.services = null;
	}

}
