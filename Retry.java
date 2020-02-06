package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Retry extends State {
	
	private static int count;
	
	public static void resetCount() {
		count = 0;
	}
	
	private State previousState;

	public Retry(TransferOperation transferOperation, State previousState) {
		super();
		this.previousState = previousState;
	}

	@Override
	public void process(Sibs sibs, TransferOperation transferOperation) throws AccountException {	
		count++;
		
		if (! (transferOperation.getState() instanceof Retry)) {
			resetCount();
			return;
		} else if (count == 3) {
			transferOperation.setState(this.previousState);
			 if (this.previousState instanceof Withdrawn) {
				 sibs.services.deposit(transferOperation.getSourceIban(), transferOperation.getValue());
			} else if (this.previousState instanceof Deposited) {
				sibs.services.withdraw(transferOperation.getTargetIban(), transferOperation.getValue());
				sibs.services.deposit(transferOperation.getSourceIban(), transferOperation.getValue());
			}
			transferOperation.setState(new ErrorState());
			resetCount();
		} else if (count < 3) {
			transferOperation.setState(this.previousState);
		}
	}
	
	@Override
	public void cancel(Sibs sibs, TransferOperation transferOperation) throws AccountException {
	}
	
}
