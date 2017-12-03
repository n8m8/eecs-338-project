public class CustomerAccount{
	private int permissions;
	private Float balance;
	public CustomerAccount(int permissions, float balance){
		this.balance = balance;
		this.permissions = permissions;
	}

	public Float getBalance(){
		return balance;
	}

	public int getPermissions(){
		return permissions;
	}
	public void withdraw(Float transactionAmount){
		balance = balance -transactionAmount;
	}
	public void deposit(Float transactionAmount){
		balance = balance + transactionAmount;
	}
	public void changePermissions(int permissions){
		this.permissions = permissions;
	}
}