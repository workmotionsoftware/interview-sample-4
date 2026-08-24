export default function OrderRow({ order }) {
  return (
    <div className="order-row">
      <span className="order-number">{order.orderNumber}</span>
      <span className="order-total">{order.total}</span>
    </div>
  );
}
