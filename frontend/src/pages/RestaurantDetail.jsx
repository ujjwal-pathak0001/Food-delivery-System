import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { client } from '../api/client';
import { useAuth } from '../context/AuthContext';
import BackButton from '../components/BackButton';
import styles from './RestaurantDetail.module.css';

export default function RestaurantDetail() {
  const { id } = useParams();
  const { isAuthenticated } = useAuth();
  const [restaurant, setRestaurant] = useState(null);
  const [menu, setMenu] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [adding, setAdding] = useState(null);
  const [toast, setToast] = useState('');

  useEffect(() => {
    let cancelled = false;
    (async () => {
      setLoading(true);
      setError('');
      try {
        const [rRes, mRes] = await Promise.all([
          client.get(`/restaurants/${id}`),
          client.get(`/restaurants/${id}/menu`),
        ]);
        if (!cancelled) {
          setRestaurant(rRes.data);
          setMenu(mRes.data);
        }
      } catch {
        if (!cancelled) setError('Restaurant not found or API unavailable.');
      } finally {
        if (!cancelled) setLoading(false);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, [id]);

  async function addToCart(menuItemId) {
    if (!isAuthenticated) {
      setToast('Log in to add items to cart.');
      return;
    }
    setAdding(menuItemId);
    setToast('');
    try {
      await client.post('/cart/items', { menuItemId, quantity: 1 });
      setToast('Added to cart');
      setTimeout(() => setToast(''), 2000);
    } catch (e) {
      setToast(e.response?.data?.error || 'Could not update cart.');
    } finally {
      setAdding(null);
    }
  }

  if (loading) return <p className={styles.muted}>Loading…</p>;
  if (error) return <p className={styles.error}>{error}</p>;
  if (!restaurant) return null;

  return (
    <div>
      <BackButton to="/" />
      <header className={styles.header}>
        <img src={restaurant.imageUrl} alt="" className={styles.heroImg} />
        <div className={styles.headerText}>
          <h1 className={styles.name}>{restaurant.name}</h1>
          <p className={styles.cuisine}>{restaurant.cuisine}</p>
          <div className={styles.meta}>
            <span>★ {restaurant.rating?.toFixed(1)}</span>
            <span>{restaurant.deliveryTimeMinutes} min delivery</span>
            <span>{restaurant.area}</span>
          </div>
          <p className={styles.desc}>{restaurant.description}</p>
        </div>
      </header>

      {toast && <p className={styles.toast}>{toast}</p>}

      <h2 className={styles.menuTitle}>Menu</h2>
      <ul className={styles.menuList}>
        {menu.map((item) => (
          <li key={item.id} className={styles.menuRow}>
            <div className={styles.menuInfo}>
              <span
                className={item.vegetarian ? styles.vegDot : styles.nonVegDot}
                title={item.vegetarian ? 'Vegetarian' : 'Non-vegetarian'}
              />
              <div>
                <div className={styles.itemName}>{item.name}</div>
                {item.description && (
                  <div className={styles.itemDesc}>{item.description}</div>
                )}
                <div className={styles.price}>₹ {Number(item.price).toFixed(0)}</div>
              </div>
            </div>
            <button
              type="button"
              className={styles.addBtn}
              disabled={adding === item.id}
              onClick={() => addToCart(item.id)}
            >
              {adding === item.id ? '…' : 'ADD'}
            </button>
          </li>
        ))}
      </ul>

      {!isAuthenticated && (
        <p className={styles.hint}>
          <Link to="/login">Log in</Link> to add items and checkout.
        </p>
      )}
    </div>
  );
}
