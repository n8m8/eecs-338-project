import java.util.Scanner;

public class AdminTerminal extends Client{

	public static void main(String[] args) {
		AdminTerminalInstance init = new AdminTerminalInstance();
		init.run();
	}

	public static class AdminTerminalInstance extends Client{
		public String currentUsername;
		public static boolean loginState = false;
		public static Scanner in;

		/*  Note: Requests should look like this:
		name\n
		method\n
		Value1\n (account to change)
		Value (amount)
		Value3\n (destination/permissions for new customer)
		*/

		public AdminTerminalInstance() {
			super();
		}

		// main method works like a shell
		public void run() {
			in = new Scanner(System.in);
			System.out.println("You just built an Admin Terminal!\nType \"login <username>\" to log in, or type \"help\" for a list of commands.");

			while (true) {
				System.out.print("terminal> ");
				String[] input = getToks(in.nextLine());

				// help shell function
				if (input[0].equals("help")) {
					System.out.println("Here's a list of commands!\nlogin <username>\t\tLogs in the user\nlogout\t\t\t\t Logs the user out\ngetbalance <username>\t\t\t Gets a user's balance\nwithdraw <username> <amount>\t\t Withdraw some amount of money from any user's account\nmakepayment <sourceuser> <destinationuser> <amount>\t moves money from one account to another\ncreate <newusername> <permission> (where <permission>='admin' or 'customer')\t creates a user account\ndelete <username>\t deletes account");

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
					if (checkNumArgs(input, 2)) {
						getBalance(input[1]);
					} else {
						System.out.println("Incorrect arguments! Use getbalance <username>");
					}

				// withdraw function
				} else if (input[0].equals("withdraw")) {
					// TODO withdraw

				// create account function
				} else if (input[0].equals("create")) {
					if (checkNumArgs(input, 3)) {
						createUser(input[1], input[2]);
					} else {
						System.out.println("Incorrect arguments! Use create <newusername> <permissions; 'admin' or 'customer'>");
					}

				// delete account function
				} else if (input[0].equals("delete")) {
					// TODO delete
				}
			}
		}


		public void login(String username) {
			if (!loginState) {
				String response = sendData(username + "\nlogin\n");
				if (response.charAt(0) == '0') {
					System.out.println("Logged in successfully!");
					currentUsername = username;
					loginState = true;
				} else {
					System.out.println("Login failed!");
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

		public void getBalance(String usernameToGet) {
			if (loginState) {
				String response = sendData(currentUsername + "\ngetBalance\n" + usernameToGet);
				if (response.charAt(0) == '0') {
					System.out.println(usernameToGet + " 's balance is " + getToks(response)[1]);
				} else {
					System.out.println("Getting balance failed!");
				}
			} else {
				System.out.println("Please log in to do commands.");
			}
		}

		public void makePayment(String destinationUsername, float amount) {
			if (loginState) {
				//String response = sendData(c)
			}
		}

		public void withdraw(float amount) {

		}

		public void createUser(String newUsername, String permissions) {
			String response = sendData(currentUsername + "\n" + "createUser\n" + newUsername + "\n" + permissions);
			if (response.charAt(0) == '0') {
				System.out.println("User created successfully!");
			} else {
				System.out.println("User creation failed!");
			}
		}

		public void deleteUser(String userToDelete) {

		}

	}
}
