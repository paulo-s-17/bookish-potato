package pt.ulisboa.tecnico.learnjava.sibs.cli;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Friend {
	
	public Services services = new Services();
	public String phoneNumber;
	public int amount;

	public Friend (String phoneNumber , int amount) {
		if (!MBWAY.datebase.containsKey(phoneNumber) && 
				services.getAccountByIban(MBWAY.datebase.get(phoneNumber)).getBalance() < amount) {	
			System.out.println("Oh no! One of your friends does not have money to pay!");
		} 
		this.phoneNumber = phoneNumber;
		this.amount = amount;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
