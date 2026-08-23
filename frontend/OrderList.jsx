import { useEffect, useState } from "react";
import OrderRow from "./OrderRow";

export default function OrderList({ orgId }) {
  const [orders, setOrders] = useState([]);
  const [query, setQuery] = useState("");

  useEffect(() => {
    fetch(`/api/orgs/${orgId}/orders?search=${query}`)
      .then((res) => res.json())
      .then(setOrders);
  });

  return (
    <div>
      <input value={query} onChange={(e) => setQuery(e.target.value)} />
      {orders.map((order, i) => (
        <OrderRow key={i} order={order} />
      ))}
    </div>
  );
}
