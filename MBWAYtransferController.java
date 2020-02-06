package pt.ulisboa.tecnico.learnjava.sibs.cli;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MBWAYtransferController {

	public void MBWAYtransfer(String sourcePhoneNumber, String targetPhoneNumber, int amount) throws SibsException, AccountException, OperationException {
		Services services = new Services();
		Sibs sibs = new Sibs(100, services);
		if(MBWAY.datebase.containsKey(sourcePhoneNumber) &&  MBWAY.datebase.containsKey(targetPhoneNumber)) {
			sibs.transfer(MBWAY.datebase.get(sourcePhoneNumber), MBWAY.datebase.get(targetPhoneNumber), amount);
			sibs.processOperation();
			System.out.println("Transfer successful!");
		} else {
			System.out.println("Could not complete transfer!");	
		}
	}
	
}
