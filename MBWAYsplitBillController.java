package pt.ulisboa.tecnico.learnjava.sibs.cli;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MBWAYsplitBillController {
	
	public Friend [] friends;
	public Services services = new Services();
	public int count = 0 ;
	
	public void resetCount() {
		this.count = 0;
	}
	
	public Friend[] getFriends() {
		return friends;
	}

	public void setFriends(Friend[] friends) {
		this.friends = friends;
	}

	public void addFriend (String phoneNumber , int amount) {
		Friend friend = new Friend(phoneNumber, amount);
		this.friends[count] = friend;
		count ++;
	}
	
	public void MBWAYsplitBill(int numberOfFriends, int amount) throws SibsException, AccountException, OperationException {
		if (count == numberOfFriends) {
			String target = friends[0].phoneNumber;
			for (int i = 1; i < numberOfFriends; i++) {
				if (correctAmount(numberOfFriends, amount) == false) {
					System.out.println("Something is wrong. Did you set the bill amount right?");
					return;
				}
				String source = friends[i].phoneNumber;
				MBWAYtransfer(source, target, friends[i].amount);
			}
			System.out.println("Bill payed successfully!");
			resetCount();
		} else if (count < numberOfFriends){
			System.out.println("Oh no! One friend is missing.");
			resetCount();
		} else if (count > numberOfFriends) {
			System.out.println("Oh no! Too many friends.");
			resetCount();
		}
	}
	
	public void MBWAYtransfer(String sourcePhoneNumber, String targetPhoneNumber, int amount) throws SibsException, AccountException, OperationException {
		Services services = new Services();
		Sibs sibs = new Sibs(100, services);
		if(MBWAY.datebase.containsKey(sourcePhoneNumber) &&  MBWAY.datebase.containsKey(targetPhoneNumber)) {
			sibs.transfer(MBWAY.datebase.get(sourcePhoneNumber), MBWAY.datebase.get(targetPhoneNumber), amount);
			sibs.processOperation();
		} else {
			System.out.println("Could not complete transfer!");
		}
	}
	
	public boolean correctAmount(int numberOfFriends, int amount) {
		int count = 0;
		for (int i = 0; i < numberOfFriends; i++) {
			count = count + friends[i].amount;
		}
		if (count == amount) {
			return true;
		} else {
			return false;
		}
	}
	
}
