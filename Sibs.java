package pt.ulisboa.tecnico.learnjava.sibs.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Sibs {
	final Operation[] operations;
	Services services;
	private static Set<TransferOperation> transferOperations= new HashSet<TransferOperation>(); 

	public Sibs(int maxNumberOfOperations, Services services) {
		this.operations = new Operation[maxNumberOfOperations];
		this.services = services;
	}

	public void transfer(String sourceIban, String targetIban, int amount)
			throws SibsException, AccountException, OperationException {
		if (services.accountValidation(sourceIban, targetIban) == false) {
			throw new SibsException();
		}
		int position = addOperation(Operation.OPERATION_TRANSFER, sourceIban, targetIban, amount);
		TransferOperation transferOperation = (TransferOperation) getOperation(position);
		transferOperations.add(transferOperation);
	}
	
	public void processOperation() throws AccountException {
		for (TransferOperation operation : transferOperations) {
			while(!(operation.getState() instanceof Completed  ||
					operation.getState() instanceof ErrorState ||
					operation.getState() instanceof Cancelled)){
				operation.getState().process(this, operation);
			}
		}
	}
	
	public void cancelOperation(int position) throws AccountException, SibsException {
		Operation operation =  getOperation(position);
		if(operation instanceof TransferOperation) {
			((TransferOperation) operation).getState().cancel(this, (TransferOperation) operation);
		}
	}

	public int addOperation(String type, String sourceIban, String targetIban, int value)
			throws OperationException, SibsException {
		int position = -1;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] == null) {
				position = i;
				break;
			}
		} if (position == -1) {
			throw new SibsException();
		}
		Operation operation;
		if (type.equals(Operation.OPERATION_TRANSFER)) {
			operation = new TransferOperation(sourceIban, targetIban, value);
			
		} else {
			operation = new PaymentOperation(targetIban, value);
		}
		this.operations[position] = operation;
		return position;
	}

	public void removeOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		this.operations[position] = null;
	}

	public Operation getOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		return this.operations[position];
	}

	public int getNumberOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result++;
			}
		}
		return result;
	}

	public int getTotalValueOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}

	public int getTotalValueOfOperationsForType(String type) {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null && this.operations[i].getType().equals(type)) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}
	
}
