import { Container, Nav } from 'react-bootstrap';
import fontStyle from '../../themes/FontFamilies';
import {
  gradeSidebarButtonWidth,
  minMaterialsButtonWidth,
} from '../../themes/Sizes';
import styles from './NavBarSecondFragmentStyles';
import 'bootstrap-icons/font/bootstrap-icons.css';

/**
 * NavBarSecondFragment renders:
 * - a search input field
 * - a search icon at the end of the input
 * - a button which will toggle the login modal
 * - when the width of the viewport becomes too small, the elements above are replaced by a search and a person icons
 * - a search modal with a search input field and icon (toggled by the search icon in the second case)
 * Props:
 * - dimensions: object ( contains the current height and width of the viewport )
 * - toggleSearchModal: function ( toggles the search modal )
 * - searchModalActive: boolean ( the search modal's visibility )
 */
export default function NavBarSecondFragment(props) {
  //Props
  const dimensions = props.dimensions;

  return (
    <>
      {(gradeSidebarButtonWidth * dimensions.width) / 100 >=
      minMaterialsButtonWidth ? (
        <Nav style={styles.navBarSecondFragment}>
          <input
            style={styles.searchInputField(dimensions.height, false)}
            type="text"
            placeholder="search bar"
          />
          <Container style={styles.searchIconBorder(dimensions.height)}>
            <i style={styles.searchIcon} className="bi bi-search"></i>
          </Container>
          <button style={styles.toggleAuthenticationButton}>
            <span
              style={{
                ...fontStyle.body_semibold,
                fontSize: '1rem',
                color: '#fff',
              }}
            >
              Autentificare
            </span>
          </button>
        </Nav>
      ) : (
        <Nav style={styles.navBarSecondFragment}>
          <i
            style={styles.searchIconSingle}
            className="bi bi-search"
            onClick={props.toggleSearchModal}
          ></i>
          <i style={styles.loginIcon} class="bi bi-person"></i>
        </Nav>
      )}

      <Nav
        style={styles.searchModal(props.searchModalActive, dimensions.height)}
      >
        <input
          style={styles.searchInputField(dimensions.height, true)}
          type="text"
          placeholder="search bar"
          onClick={() => {
            console.log('click');
          }}
        />
        <Container style={styles.searchIconBorder(dimensions.height)}>
          <i style={styles.searchIcon} className="bi bi-search"></i>
        </Container>
      </Nav>
    </>
  );
}
