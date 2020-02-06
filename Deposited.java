package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Deposited extends State {

	@Override
	public void process(Sibs sibs, TransferOperation transferOperation) throws AccountException {
		try {
			sibs.services.withdraw(transferOperation.getSourceIban(), transferOperation.commission());
			transferOperation.setState(new Completed());	
		} catch (AccountException accountException) {
			sibs.services.withdraw(transferOperation.getTargetIban(), transferOperation.getValue());
			sibs.services.deposit(transferOperation.getSourceIban(), transferOperation.getValue());
			transferOperation.setState(new Retry(transferOperation,this));
		}
	}
	
	@Override
	public void cancel(Sibs sibs, TransferOperation transferOperation) throws AccountException {
		sibs.services.withdraw(transferOperation.getTargetIban(), transferOperation.getValue());
		sibs.services.deposit(transferOperation.getSourceIban(), transferOperation.getValue());
		transferOperation.setState(new Cancelled());
	}

}
