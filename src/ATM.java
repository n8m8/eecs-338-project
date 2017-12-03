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

				// help shell function
				if (input[0].equals("help")) {
					System.out.println("Here's a list of commands!\nlogin <username>\t\tLogs in the user\nlogout\t\t\t\t Logs the user out\ngetbalance\t\t\t Gets your balance if you're logged in\nwithdraw <amount>\t\t Withdraw some amount of money");

				// login shell function
				} else if (input[0].equals("login")) {
					if (checkNumArgs(input, 2)) {
						login(input[1]);
					} else {
						System.out.println("Incorrect arguments! Use login <username>");
					}

				// logout shell function
				} else if (input[0].equals("logout")) {
					// TODO logout
					logout();

				// get balance shell function
				} else if (input[0].equals("getbalance")) {
					// TODO getbalance

				// withdraw function
				} else if (input[0].equals("withdraw")) {
					// TODO withdraw
				}
			}
		}

		public void login(String username) {
			if (!loginState) {
				String response = sendData(username + "\nlogin\n");
				if (response.substring(0, 5).toLowerCase().equals("error")) {

				} else {
					loginState = true;
					currentUsername = username;
					System.out.println("[debug] response was:" + response);
					System.out.println("Successfully logged in.");
				}
			}
		}

		public void logout() {
			if (loginState) {
				currentUsername = "";
				loginState = false;
			} else {
				System.out.println("You can't log out if you're not logged in!");
			}
		}

		public void getBalance() {

		}

		public void withdraw(float amount) {

		}
	}

}
