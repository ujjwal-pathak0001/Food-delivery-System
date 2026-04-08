import { useEffect, useState } from 'react';
import { client } from '../api/client';
import BackButton from '../components/BackButton';
import styles from './OrdersPage.module.css';

const statusLabels = {
  PLACED: 'Order placed',
  CONFIRMED: 'Confirmed',
  OUT_FOR_DELIVERY: 'On the way',
  DELIVERED: 'Delivered',
  CANCELLED: 'Cancelled',
};

export default function OrdersPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        const { data } = await client.get('/orders');
        if (!cancelled) setOrders(data);
      } catch {
        if (!cancelled) setError('Could not load orders.');
      } finally {
        if (!cancelled) setLoading(false);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, []);

  if (loading) return <p className={styles.muted}>Loading orders…</p>;
  if (error) return <p className={styles.error}>{error}</p>;

  if (orders.length === 0) {
    return (
      <div>
        <BackButton to="/" />
        <h1 className={styles.title}>Your orders</h1>
        <p className={styles.empty}>
          No orders yet. <Link to="/">Order something delicious</Link>
        </p>
      </div>
    );
  }

  return (
    <div>
      <BackButton to="/" />
      <h1 className={styles.title}>Your orders</h1>
      <ul className={styles.list}>
        {orders.map((o) => (
          <li key={o.id} className={styles.card}>
            <div className={styles.cardTop}>
              <div>
                <div className={styles.restName}>{o.restaurantName}</div>
                <div className={styles.status}>{statusLabels[o.status] || o.status}</div>
              </div>
              <div className={styles.total}>₹ {Number(o.totalAmount).toFixed(0)}</div>
            </div>
            <div className={styles.meta}>
              {new Date(o.placedAt).toLocaleString()} · {o.deliveryAddress}
            </div>
            <ul className={styles.items}>
              {o.items?.map((it, idx) => (
                <li key={`${o.id}-${idx}`}>
                  {it.itemName} × {it.quantity}
                </li>
              ))}
            </ul>
          </li>
        ))}
      </ul>
    </div>
  );
}
