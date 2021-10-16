import { Container } from 'react-bootstrap';

import NavMenu from '../navMenu/NavMenu';
import styles from './LayoutStyle';

//This component will render the NavBar and the content of the current page
export default function Layout(props) {
  return (
    <>
      <NavMenu />
      <Container fluid style={styles.contentStyle}>
        {props.children}
      </Container>
    </>
  );
}
