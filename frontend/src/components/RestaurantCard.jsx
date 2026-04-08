import { Link } from 'react-router-dom';
import styles from './RestaurantCard.module.css';

export default function RestaurantCard({ restaurant }) {
  const { id, name, cuisine, rating, deliveryTimeMinutes, imageUrl, area } = restaurant;
  return (
    <Link to={`/restaurant/${id}`} className={styles.card}>
      <div className={styles.imageWrap}>
        <img src={imageUrl} alt="" className={styles.img} loading="lazy" />
      </div>
      <div className={styles.body}>
        <h3 className={styles.title}>{name}</h3>
        <p className={styles.cuisine}>{cuisine}</p>
        <div className={styles.meta}>
          <span className={styles.rating}>★ {rating?.toFixed(1)}</span>
          <span className={styles.dot}>·</span>
          <span>{deliveryTimeMinutes} min</span>
          <span className={styles.dot}>·</span>
          <span>{area}</span>
        </div>
      </div>
    </Link>
  );
}
