package pt.ulisboa.tecnico.learnjava.sibs.cli;

import java.util.Random;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class associateMBWAYController {

	public void associateMBWAY (String iban, String phoneNumber) {
		Services service = new Services();
		Random rand = new Random();
		int code = 100000 + rand.nextInt(899999); 
		if(service.getAccountByIban(iban).getClient().getPhoneNumber().equals(phoneNumber)) {
			service.getAccountByIban(iban).getClient().setMbayCode(code);
			MBWAY.datebase.put(phoneNumber, iban);
			System.out.println("Code: " + code + " (don’t share it with anyone)");
		} else {
			System.out.println("Wrong phone number!");
		}
	}
	
}
