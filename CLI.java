package pt.ulisboa.tecnico.learnjava.sibs.cli;


import java.util.Scanner;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class CLI {
	
	public void exit() {
		System.exit(0);
	}
	
	public static void main(String[] args) throws BankException, ClientException, AccountException, 
											NumberFormatException, SibsException, OperationException {
		
		Scanner input = new Scanner(System.in);
		String command = input.next();
		CLI cli = new CLI();
		associateMBWAYController controller1 = new associateMBWAYController();
		confirmMBWAYController controller2 = new confirmMBWAYController();
		MBWAYtransferController controller3 = new MBWAYtransferController();
		MBWAYsplitBillController controller4 = new MBWAYsplitBillController();
		Services service = new Services();
		Sibs sibs = new Sibs(100, service);
		Bank bank = new Bank("CGD");
		Client client1 = new Client(bank, "Joao", "Duarte", "123456789", "123456789", "Lisboa", 33);
		Client client2 = new Client(bank, "Paulo", "Sampaio", "123456788", "123456788", "Porto", 33);
		Client client3 = new Client(bank, "Paulo", "Sampaio", "123456787", "123456787", "Porto", 33);
		bank.createAccount(AccountType.CHECKING, client1, 100, 0);
		bank.createAccount(AccountType.CHECKING, client2, 100, 0);
		bank.createAccount(AccountType.CHECKING, client3, 100, 0);
		
		while (true) {
			if(command.equals("exit")) {
				cli.exit();
			} else if (command.startsWith("associate-mbway")) {
				controller1.associateMBWAY(input.next(), input.next());
			} else if (command.startsWith("confirm-mbway")) {
				controller2.confirmMBWAY(input.next(), Integer.valueOf(input.next()));
			} else if (command.startsWith("mbway-transfer")) {
				controller3.MBWAYtransfer(input.next(),  input.next(), Integer.valueOf(input.next()));
			} else if (command.startsWith("mbway-split-bill")) {
				int numberOfFriends = Integer.valueOf(input.next());
				int totalAmount = Integer.valueOf(input.next());
				Friend friends[] = new Friend[100];
				controller4.setFriends(friends);
				while(true) {
					String command2 = input.next();
					if (command2.startsWith("friend")) {
						controller4.addFriend(input.next(), Integer.valueOf(input.next()));	
					} else if(command2.equals("end")) {
						break;
					}
				}
				controller4.MBWAYsplitBill(numberOfFriends, totalAmount);
			} else {
				System.out.println("Command not correct!");
			}
			command = input.next();
		}
		
	}

}
