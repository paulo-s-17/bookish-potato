package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public abstract class State {

		public void process(Sibs sibs, TransferOperation transferOperation) throws AccountException {
		}
		
		public void cancel(Sibs sibs, TransferOperation transferOperation) throws AccountException {
		}
		
}
