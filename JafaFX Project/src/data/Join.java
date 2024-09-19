package data;

public class Join {
	private String transactionId, juiceId, juiceName;
	private int quantity;

	public Join(String transactionId, String juiceId, String juiceName, int quantity) {
		super();
		this.transactionId = transactionId;
		this.juiceId = juiceId;
		this.juiceName = juiceName;
		this.quantity = quantity;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getJuiceId() {
		return juiceId;
	}

	public void setJuiceId(String juiceId) {
		this.juiceId = juiceId;
	}

	public String getJuiceName() {
		return juiceName;
	}

	public void setJuiceName(String juiceName) {
		this.juiceName = juiceName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
