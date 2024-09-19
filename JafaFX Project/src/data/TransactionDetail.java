package data;

public class TransactionDetail {
	String transactionId, juiceId;
	int quantity;

	public TransactionDetail(String transactionId, String juiceId, int quantity) {
		super();
		this.transactionId = transactionId;
		this.juiceId = juiceId;
		this.quantity = quantity;
	}

	String getTransactionId() {
		return transactionId;
	}

	void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	String getJuiceId() {
		return juiceId;
	}

	void setJuiceId(String juiceId) {
		this.juiceId = juiceId;
	}

	int getQuantity() {
		return quantity;
	}

	void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
