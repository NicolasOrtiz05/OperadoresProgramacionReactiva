package com.example;

import io.reactivex.Observable;

public class ProductRequestProcessor {
	public static void main(String[] args) {
		// Flujo de datos simulando las solicitudes de productos
		Observable<ProductRequest> productRequests = Observable.just(
				new ProductRequest("tecnología", 5),
				new ProductRequest("hogar", 2),
				new ProductRequest("tecnología", 10),
				new ProductRequest("juguetes", 3));

		// Precio fijo por producto
		int pricePerProduct = 20;

		productRequests
				// Transforma cada solicitud para obtener el precio total
				.map(request -> {
					request.setTotalPrice(request.getQuantity() * pricePerProduct);
					return request;
				})
				// Filtra solicitudes con al menos 5 productos
				.filter(request -> request.getQuantity() >= 5)
				// Simula llamada a API para obtener estado del pedido
				.flatMap(request -> Observable.just(request).map(req -> {
					req.setStatus("Procesado");
					return req;
				}))
				// Combina solicitudes de diferentes categorías en un solo flujo
				.mergeWith(Observable.just(new ProductRequest("deportes", 7)))
				// Empareja cada solicitud con su estado final
				.zipWith(Observable.just("Completado"), (request, status) -> {
					request.setStatus(status);
					return request;
				})
				// Suscripción y muestra del resultado final
				.subscribe(System.out::println);
	}

	static class ProductRequest {
		private String category;
		private int quantity;
		private int totalPrice;
		private String status;

		public ProductRequest(String category, int quantity) {
			this.category = category;
			this.quantity = quantity;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setTotalPrice(int totalPrice) {
			this.totalPrice = totalPrice;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		@Override
		public String toString() {
			return "ProductRequest{" +
					"category='" + category + '\'' +
					", quantity=" + quantity +
					", totalPrice=" + totalPrice +
					", status='" + status + '\'' +
					'}';
		}
	}
}
