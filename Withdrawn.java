package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Withdrawn extends State{
	
	@Override
	public void process(Sibs sibs, TransferOperation transferOperation) throws AccountException {
		try {
			sibs.services.deposit(transferOperation.getTargetIban(), transferOperation.getValue());
			if (sibs.services.equalBankValidation(transferOperation.getSourceIban(), transferOperation.getTargetIban())) {
				transferOperation.setState(new Completed());
			} else {
				transferOperation.setState(new Deposited());	
			}
		} catch (AccountException accountException) {
			sibs.services.deposit(transferOperation.getSourceIban(), transferOperation.getValue());
			transferOperation.setState(new Retry(transferOperation,this));
		}
	}
	
	@Override
	public void cancel(Sibs sibs, TransferOperation transferOperation) throws AccountException {
		sibs.services.deposit(transferOperation.getSourceIban(), transferOperation.getValue());
		transferOperation.setState(new Cancelled());
	}

}
