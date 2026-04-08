import { useNavigate } from 'react-router-dom';
import styles from './BackButton.module.css';

export default function BackButton({ to = '/' }) {
  const navigate = useNavigate();

  const handleBack = () => {
    navigate(to);
  };

  return (
    <button className={styles.backBtn} onClick={handleBack} title="Go back">
      <span className={styles.arrow}>←</span> Back
    </button>
  );
}
