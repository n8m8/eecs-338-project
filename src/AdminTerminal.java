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
					System.out.println("Here's a list of commands!\nlogin <username>\t\tLogs in the user\nlogout\t\t\t\t Logs the user out\ngetbalance <username>\t\t\t Gets a user's balance\nwithdraw <username> <amount>\t\t Withdraw some amount of money from any user's account\nmakepayment <sourceuser> <destinationuser> <amount>\t moves money from one account to another\ncreate <newusername> <permission> (where <permission>='admin' or 'customer')\t creates a user account\ndelete <username>\t deletes account\nexit\t\t Exits the program");

				// login shell function
				} else if (input[0].equals("login")) {
					if (checkNumArgs(input, 2)) {
						login(input[1]);
					} else {
						System.out.println("Incorrect arguments! Use login <username>");
					}

				// logout shell function
				} else if (input[0].equals("logout")) {
					logout();

				// get balance shell function
				} else if (input[0].equals("getbalance")) {
					if (checkNumArgs(input, 2)) {
						getBalance(input[1]);
					} else {
						System.out.println("Incorrect arguments! Use getbalance <username>");
					}

				// Move money from one account to another
				} else if (input[0].equals("makepayment")) {
					if (checkNumArgs(input, 4)) {
						makePayment(input[1], input[2], Float.parseFloat(input[3]));
					}
					
				// withdraw function
				} else if (input[0].equals("withdraw")) {
					if (checkNumArgs(input, 3)) {
						withdraw(input[1], Float.parseFloat(input[2]));
					} else {
						System.out.println("Incorrect arguments! Use withdraw <username> <amount>");
					}

				} else if (input[0].equals("deposit")) {
					if (checkNumArgs(input, 3)) {
						deposit(input[1], Float.parseFloat(input[2]));
					} else {
						System.out.println("Incorrect arguments! Use deposit <username> <amount>");
					}

				// create account function
				} else if (input[0].equals("create")) {
					if (checkNumArgs(input, 3)) {
						createUser(input[1], input[2]);
					} else {
						System.out.println("Incorrect arguments! Use create <newusername> <permissions; 'admin' or 'customer'>");
					}

				// delete account function
				} else if (input[0].equals("delete")) {
					if (checkNumArgs(input, 2)) {
						deleteUser(input[1]);
					} else {
						System.out.println("Incorrect arguments! Use delete <username>");
					}
					
				} else if (input[0].equals("exit")) {
					System.out.println("Goodbye!");
					System.exit(0);
					
				} else {
					System.out.println("That's not a command! Type help for help.");
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
					System.out.println(usernameToGet + "'s balance is " + getToks(response)[1]);
				} else {
					System.out.println("Getting balance failed!");
				}
			} else {
				System.out.println("Please log in to do commands.");
			}
		}

		public void makePayment(String sourceUsername, String destinationUsername, float amount) {
			if (loginState) {
				String response = sendData(currentUsername + "\nmakePayment\n" + sourceUsername + "\n" + destinationUsername + "\n" + amount);
				
				if (response.charAt(0) == '0') {
					System.out.println("Money transfered successfully!");
					String newBalance = sendData(currentUsername + "\ngetBalance\n" + currentUsername);
					System.out.println("Your new balance is " + getToks(newBalance)[1]);
				} else {
					System.out.println("Unable to transfer money: " + response.charAt(0));
				}
			}
		}

		public void withdraw(String userToTake, float amount) {
			if (loginState) {
				String response = sendData(currentUsername + "\nwithdraw\n" + userToTake + "\n" + amount);
				if (response.charAt(0) == '0') {
					System.out.println("Success! You just withdrew " + amount + " cash.");
					String newBalResp = sendData(currentUsername + "\ngetBalance\n" + userToTake);
					if (newBalResp.charAt(0) == '0') {
						System.out.println(userToTake + "'s new balance is " + getToks(newBalResp)[1]);
					} else {
						System.out.println("Failed to get " + userToTake + "'s new balance.");
					}
				} else {
					System.out.println("Failed to execute function");
				}
			}
		}
		
		public void deposit(String userToCredit, float amount) {
			if (loginState) {
				String response = sendData(currentUsername + "\ndeposit\n" + userToCredit + "\n" + amount);
				if (response.charAt(0) == '0') {
					System.out.println(userToCredit + "'s account credited successfully!");
					String newBalResp = sendData(currentUsername + "\ngetBalance\n" + userToCredit);
					if (newBalResp.charAt(0) == '0') {
						System.out.println(userToCredit + "'s balance is now " + getToks(newBalResp)[1]);
					} else {
						System.out.println("Unable to get " + userToCredit + "'s new balance.");
					}
				}
			}
		}

		public void createUser(String newUsername, String permissions) {
			String response = sendData(currentUsername + "\ncreateUser\n" + newUsername + "\n" + permissions);
			if (response.charAt(0) == '0') {
				System.out.println("User created successfully!");
			} else {
				System.out.println("User creation failed!");
			}
		}

		public void deleteUser(String userToDelete) {
			String response = sendData(currentUsername + "\ndeleteUser\n" + userToDelete);
			if (response.charAt(0) == '0') {
				System.out.println("Account terminated successfully.");
			} else {
				System.out.println("Unable to terminate account!" + getToks(response)[1]);
			}
		}

	}
}
