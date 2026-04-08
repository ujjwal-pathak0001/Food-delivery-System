import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { client } from '../api/client';
import BackButton from '../components/BackButton';
import styles from './CartPage.module.css';

export default function CartPage() {
  const navigate = useNavigate();
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const [address, setAddress] = useState('');
  const [placing, setPlacing] = useState(false);
  const [error, setError] = useState('');

  async function refresh() {
    const { data } = await client.get('/cart');
    setCart(data);
  }

  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        await refresh();
      } catch {
        if (!cancelled) setError('Could not load cart.');
      } finally {
        if (!cancelled) setLoading(false);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, []);

  async function updateQty(cartItemId, quantity) {
    setError('');
    try {
      const { data } = await client.patch('/cart/items', { cartItemId, quantity });
      setCart(data);
    } catch (e) {
      setError(e.response?.data?.error || 'Update failed.');
    }
  }

  async function placeOrder(e) {
    e.preventDefault();
    if (!address.trim()) {
      setError('Enter a delivery address.');
      return;
    }
    setPlacing(true);
    setError('');
    try {
      await client.post('/orders', { deliveryAddress: address.trim() });
      navigate('/orders');
    } catch (e) {
      setError(e.response?.data?.error || 'Could not place order.');
    } finally {
      setPlacing(false);
    }
  }

  if (loading) return <p className={styles.muted}>Loading cart…</p>;

  const items = cart?.items || [];
  const subtotal = cart?.subtotal != null ? Number(cart.subtotal) : 0;

  return (
    <div>
      <BackButton to="/" />
      <h1 className={styles.title}>Your cart</h1>
      {items.length === 0 ? (
        <p className={styles.empty}>
          Cart is empty. <Link to="/">Browse restaurants</Link>
        </p>
      ) : (
        <>
          <ul className={styles.list}>
            {items.map((line) => (
              <li key={line.id} className={styles.row}>
                <div>
                  <div className={styles.lineName}>
                    {line.vegetarian ? (
                      <span className={styles.veg} title="Veg" />
                    ) : (
                      <span className={styles.nonveg} title="Non-veg" />
                    )}
                    {line.name}
                  </div>
                  <div className={styles.rest}>from {line.restaurantName}</div>
                  <div className={styles.priceLine}>
                    ₹ {Number(line.unitPrice).toFixed(0)} × {line.quantity}
                  </div>
                </div>
                <div className={styles.qty}>
                  <button
                    type="button"
                    className={styles.qtyBtn}
                    onClick={() => updateQty(line.id, line.quantity - 1)}
                    aria-label="Decrease"
                  >
                    −
                  </button>
                  <span>{line.quantity}</span>
                  <button
                    type="button"
                    className={styles.qtyBtn}
                    onClick={() => updateQty(line.id, line.quantity + 1)}
                    aria-label="Increase"
                  >
                    +
                  </button>
                </div>
              </li>
            ))}
          </ul>
          <div className={styles.summary}>
            <div className={styles.subtotalRow}>
              <span>Subtotal</span>
              <span>₹ {subtotal.toFixed(0)}</span>
            </div>
            <form onSubmit={placeOrder} className={styles.checkout}>
              <label className={styles.label}>
                Delivery address
                <textarea
                  required
                  rows={3}
                  value={address}
                  onChange={(e) => setAddress(e.target.value)}
                  placeholder="Flat, street, landmark, city"
                  className={styles.textarea}
                />
              </label>
              {error && <p className={styles.error}>{error}</p>}
              <button type="submit" className={styles.payBtn} disabled={placing}>
                {placing ? 'Placing order…' : 'Place order & pay on delivery'}
              </button>
            </form>
          </div>
        </>
      )}
    </div>
  );
}
