package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Registered extends State {

	@Override
	public void process(Sibs sibs, TransferOperation transferOperation) throws AccountException {
		try {
			sibs.services.withdraw(transferOperation.getSourceIban(), transferOperation.getValue());
			transferOperation.setState(new Withdrawn());
		} catch (AccountException accountException) {
			transferOperation.setState(new Retry(transferOperation,this));
		}
	}
	
	@Override
	public void cancel(Sibs sibs, TransferOperation transferOperation) throws AccountException {
		transferOperation.setState(new Cancelled());
	}

}
