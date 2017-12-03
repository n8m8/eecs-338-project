import java.util.Scanner;

public class ATM extends Client {

	/*
	 * Note: Requests should look like this: 
	 * name\n 
	 * method\n 
	 * Value1\n (account to change) 
	 * Value (amount) 
	 * Value3\n (destination/permissions for new customer)
	 */

	public static void main(String[] args) {
		ATMInstance init = new ATMInstance();
		init.run();
	}

	public static class ATMInstance extends Client {
		public static String currentUsername;
		public static float cashInMachine;
		public static boolean loginState = false; // false=noone is logged in, true=user is logged in

		private static Scanner in;

		// Clients have common init stuff so need constructor calling super()
		public ATMInstance() {
			super();
		}

		// main method works like a shell
		public void run() {
			in = new Scanner(System.in);
			System.out.println("You just built an ATM!\nPutting $1000 cash into the machine.\nType \"login <username>\" to log in, or type \"help\" for a list of commands.");
			cashInMachine = 1000;
			while (true) {
				System.out.print("atm> ");
				String[] input = getToks(in.nextLine());

				// help
				if (input[0].equals("help")) {
					System.out.println("Here's a list of commands!\nlogin <username>\t\tLogs in the user\nlogout\t\t\t\t Logs the user out\ngetbalance\t\t\t Gets your balance if you're logged in\nwithdraw <amount>\t\t Withdraw some amount of money");

					// login
				} else if (input[0].equals("login")) {
					if (checkNumArgs(input, 2)) {
						String response = sendData(input[1] + "\nlogin\n");
						if (response.substring(0, 5).toLowerCase().equals("error")) {

						} else {
							loginState = true;
							currentUsername = input[1];
							System.out.println("[debug] response was:" + response);
							System.out.println("Successfully logged in.");
						}
					} else {
						System.out.println("Incorrect arguments! Use login <username>");
					}

					// logout
				} else if (input[0].equals("logout")) {
					// TODO logout

					// get balance
				} else if (input[0].equals("getbalance")) {
					// TODO getbalance

					// withdraw
				} else if (input[0].equals("withdraw")) {
					// TODO withdraw
				}
			}
		}

		public void login(String username) {
			if (loginState == false) {
				String response = sendData(username + "\nlogin");
				if (!response.equals("error:failure")) {
					this.currentUsername = username;
					loginState = true;
				}
			} else {
				System.out.println("You're already logged in as " + username + "! Type logout to log out.");
			}
		}

		public void logout() {

		}

		public void getBalance() {

		}

		public void withdraw(float amount) {

		}
	}

}
