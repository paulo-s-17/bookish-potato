package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Cancelled;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Withdrawn;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferOperationCancelMethodTest {
	
	private TransferOperation transferOperation;
	
	@Before
	public void setUp() throws BankException, AccountException, ClientException {
	}
	
	@Test
	public void mockCancelTestForRegisterd() throws  AccountException, SibsException, OperationException {
		Services mockSirvices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockSirvices);
		transferOperation = new TransferOperation("source", "target", 100);
		when(mockSirvices.equalBankValidation("source", "target")).thenReturn(true);
		assertTrue(transferOperation.getState() instanceof Registered);
		transferOperation.getState().cancel(sibs, transferOperation);
		assertTrue(transferOperation.getState() instanceof Cancelled);
		
	}
	
	@Test
	public void mockCancelTestForWithrawn() throws  AccountException, SibsException, OperationException {
		Services mockSirvices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockSirvices);
		transferOperation = new TransferOperation("source", "target", 100);
		when(mockSirvices.equalBankValidation("source", "target")).thenReturn(true);
		assertTrue(transferOperation.getState() instanceof Registered);
		transferOperation.getState().process(sibs, transferOperation);
		verify(mockSirvices).withdraw("source", 100);
		assertTrue(transferOperation.getState() instanceof Withdrawn);
		transferOperation.getState().cancel(sibs, transferOperation);
		assertTrue(transferOperation.getState() instanceof Cancelled);
		verify(mockSirvices).deposit("source", 100);
	}
	
	@Test
	public void mockCancelTestForDeposited() throws  AccountException, SibsException, OperationException {
		Services mockSirvices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockSirvices);
		transferOperation = new TransferOperation("source", "target", 100);
		when(mockSirvices.equalBankValidation("source", "target")).thenReturn(false);
		assertTrue(transferOperation.getState() instanceof Registered);
		transferOperation.getState().process(sibs, transferOperation);
		verify(mockSirvices).withdraw("source", 100);
		assertTrue(transferOperation.getState() instanceof Withdrawn);
		transferOperation.getState().process(sibs, transferOperation);
		verify(mockSirvices).deposit("target", 100);
		assertTrue(transferOperation.getState() instanceof Deposited);
		transferOperation.getState().cancel(sibs, transferOperation);
		assertTrue(transferOperation.getState() instanceof Cancelled);
		verify(mockSirvices).withdraw("target", 100);
		verify(mockSirvices).deposit("source", 100);
	}
	
	@Test
	public void mockCancelTestForCompleted() throws  AccountException, SibsException, OperationException {
		Services mockSirvices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockSirvices);
		transferOperation = new TransferOperation("source", "target", 100);
		when(mockSirvices.equalBankValidation("source", "target")).thenReturn(true);
		assertTrue(transferOperation.getState() instanceof Registered);
		transferOperation.getState().process(sibs, transferOperation);
		verify(mockSirvices).withdraw("source", 100);
		assertTrue(transferOperation.getState() instanceof Withdrawn);
		transferOperation.getState().process(sibs, transferOperation);
		verify(mockSirvices).deposit("target", 100);
		assertTrue(transferOperation.getState() instanceof Completed);
		verify(mockSirvices).deposit("target", 100);
		transferOperation.getState().cancel(sibs, transferOperation);
		assertTrue(transferOperation.getState() instanceof Completed);
	}
	

	@After
	public void tearDown() {
		Bank.clearBanks();
	}


}
