public class CustomerAccount{
	private int permissions;
	private Float balance;
	private Object lock = new Object();
	public CustomerAccount(int permissions, float balance){
		synchronized (lock) {
			this.balance = balance;
			this.permissions = permissions;
		}
	}

	public Float getBalance(){
		synchronized (lock) {
			return balance;
		}
	}

	public int getPermissions(){
		synchronized (lock) {
			return permissions;
		}
	}
	public void withdraw(Float transactionAmount){
		synchronized (lock) {
			balance = balance -transactionAmount;
		}
	}
	public void deposit(Float transactionAmount){
		synchronized (lock) {
			balance = balance + transactionAmount;
		}
	}
	public void changePermissions(int permissions){
		synchronized (lock) {
			this.permissions = permissions;
		}
	}
}
