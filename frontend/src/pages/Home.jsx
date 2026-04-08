import { useEffect, useState } from 'react';
import { client } from '../api/client';
import RestaurantCard from '../components/RestaurantCard';
import styles from './Home.module.css';

export default function Home() {
  const [restaurants, setRestaurants] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    let cancelled = false;
    (async () => {
      setLoading(true);
      setError('');
      try {
        const params = search.trim() ? { search: search.trim() } : {};
        const { data } = await client.get('/restaurants', { params });
        if (!cancelled) setRestaurants(data);
      } catch (e) {
        if (!cancelled) setError('Could not load restaurants. Is the API running?');
      } finally {
        if (!cancelled) setLoading(false);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, [search]);

  return (
    <div>
      <section className={styles.hero}>
        <h1 className={styles.headline}>
          Order food & groceries
          <span className={styles.accent}> near you</span>
        </h1>
        <div className={styles.searchBar}>
          <span className={styles.searchIcon}>⌕</span>
          <input
            type="search"
            placeholder="Search restaurants or cuisine"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className={styles.searchInput}
            aria-label="Search"
          />
        </div>
      </section>

      <h2 className={styles.sectionTitle}>Popular near you</h2>

      {loading && <p className={styles.muted}>Loading…</p>}
      {error && <p className={styles.error}>{error}</p>}
      {!loading && !error && (
        <ul className={styles.grid}>
          {restaurants.map((r) => (
            <li key={r.id} className={styles.gridItem}>
              <RestaurantCard restaurant={r} />
            </li>
          ))}
        </ul>
      )}
      {!loading && !error && restaurants.length === 0 && (
        <p className={styles.muted}>No restaurants match your search.</p>
      )}
    </div>
  );
}
