import { Outlet, Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import styles from './Layout.module.css';

export default function Layout() {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  return (
    <div className={styles.shell}>
      <header className={styles.header}>
        <div className={styles.headerInner}>
          <Link to="/" className={styles.brand}>
            <span className={styles.brandIcon}>◣</span>
            FoodDash
          </Link>
          <nav className={styles.nav}>
            {isAuthenticated ? (
              <>
                <span className={styles.greet}>Hi, {user?.fullName?.split(' ')[0]}</span>
                <Link to="/orders">Orders</Link>
                <Link to="/cart" className={styles.ctaOutline}>
                  Cart
                </Link>
                <button
                  type="button"
                  className={styles.linkBtn}
                  onClick={() => {
                    logout();
                    navigate('/');
                  }}
                >
                  Log out
                </button>
              </>
            ) : (
              <>
                <Link to="/login">Log in</Link>
                <Link to="/register" className={styles.cta}>
                  Sign up
                </Link>
              </>
            )}
          </nav>
        </div>
      </header>
      <main className={styles.main}>
        <Outlet />
      </main>
      <footer className={styles.footer}>
        <p>Demo food delivery UI inspired by popular apps — React + Spring Boot + PostgreSQL.</p>
      </footer>
    </div>
  );
}
