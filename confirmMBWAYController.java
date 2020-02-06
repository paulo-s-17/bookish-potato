package pt.ulisboa.tecnico.learnjava.sibs.cli;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class confirmMBWAYController {

	public void confirmMBWAY (String phoneNumber, int code) {
		Services services = new Services();
		if(MBWAY.datebase.containsKey(phoneNumber)) {
			if(services.getAccountByIban(MBWAY.datebase.get(phoneNumber)).getClient().getMbayCode() == code) {
				System.out.println("MBWay Association Confirmed Successfully!");
			} else {
				System.out.println("Wrong confirmation code!");
			}	
		} else {
			System.out.println("Wrong phone number!");
		}
	}
}
