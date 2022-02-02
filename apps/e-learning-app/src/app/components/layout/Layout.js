import { Container } from 'react-bootstrap';
import FooterFirstFragment from '../footerFirstFragment/FooterFirstFragment';

import NavMenu from '../navMenu/NavMenu';
import styles from './LayoutStyle';

/**
 * Layout renders:
 * - the nav menu
 * - the content of the current page
 *
 * Props:
 * - dimensions: object ( contains the current height and width of the viewport )
 * - children: react-component ( the current page )
 */
export default function Layout(props) {
  //Props
  const dimensions = props.dimensions;

  return (
    <>
      <NavMenu dimensions={dimensions} />
      <FooterFirstFragment dimensions={dimensions} />
      <Container fluid style={styles.contentStyle(dimensions.height)}>
        {props.children}
      </Container>
    </>
  );
}
