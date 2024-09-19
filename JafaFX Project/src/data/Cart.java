package data;

public class Cart {
	private String username, juiceId;
	private int quantity;

	public Cart(String username, String juiceId, int quantity) {
		super();
		this.username = username;
		this.juiceId = juiceId;
		this.quantity = quantity;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getJuiceId() {
		return juiceId;
	}

	public void setJuiceId(String juiceId) {
		this.juiceId = juiceId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
