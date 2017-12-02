public class CustomerAccount{
	private int balance, permissions
	public CustomerAccount(int permissions, int balance){
		this.balance = balance;
		this.permissions = permissions
	}

	public int getBalance(){
		return balance;
	}

	public int getPermissions(){
		return permissions
	}
	public void withdraw(int quantity){
		balance = balance -quantity;
	}
	public void deposit(int quantity){
		balance = balance + quantity;
	}
	public void changePermissions(int permissions){
		this.permissions = permissions;
	}
}