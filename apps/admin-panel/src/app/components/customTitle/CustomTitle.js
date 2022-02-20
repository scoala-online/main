import { Container, Row } from 'react-bootstrap';
import style from './CustomTitleStyle';

/**
 * Renders a page title with an optional divider underneath.
 * @param {*} props 
 * >
 * - `String` title
 * - `boolean` hasDivider - toggles the divider on or off.
 */
export default function CustomTitle(props) {
  const title = props.value;
  const hasDivider = props.hasDivider || true;

  return (
    <Container style={style.containerStyle}>
      <h1 class="fw-light font-monospace">{title}</h1>
      {hasDivider && <hr style={style.dividerStyle}></hr>}
    </Container>
  );
}
