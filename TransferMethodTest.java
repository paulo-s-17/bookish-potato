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
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferMethodTest {
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
	public void successProcessed() throws BankException, AccountException, SibsException, OperationException, ClientException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);
		this.sibs.transfer(sourceIban, targetIban, 100);
		this.sibs.processOperation();
		assertEquals(894, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1100, this.services.getAccountByIban(targetIban).getBalance());
		assertEquals(1, this.sibs.getNumberOfOperations());
		assertEquals(100, this.sibs.getTotalValueOfOperations());
		assertEquals(100, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
	}
	
	
	@Test
	public void mockTransferTestProcess() throws  AccountException, SibsException, OperationException {
		Services mockServices1 = mock(Services.class);
		Sibs sibs1 = new Sibs(100, mockServices1);
		when(mockServices1.accountValidation("source", "target")).thenReturn(true);
		when(mockServices1.equalBankValidation("source", "target")).thenReturn(true);
		sibs1.transfer("source", "target", 100);
		sibs1.processOperation();
		verify(mockServices1).withdraw("source", 100);
		verify(mockServices1).deposit("target", 100);
		assertEquals(sibs1.getNumberOfOperations(), 1);
	}
	
	@Test
	public void mockTransferTestBetweenDiferentBanks() throws  AccountException, SibsException, OperationException {
		Services mockTransfer = mock(Services.class);
		Sibs sibs = new Sibs(100, mockTransfer);
		when(mockTransfer.accountValidation("source", "target")).thenReturn(true);
		when(mockTransfer.equalBankValidation("source", "target")).thenReturn(false);
		sibs.transfer("source", "target", 100);
		sibs.processOperation();
		verify(mockTransfer).deposit("target", 100);
		verify(mockTransfer).withdraw("source", 100);
		verify(mockTransfer).withdraw("source", 6);
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
