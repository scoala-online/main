import { Container, Nav } from 'react-bootstrap';
import fontStyle from '../../themes/FontFamilies';
import {
  gradeSidebarButtonWidth,
  minMaterialsButtonWidth,
} from '../../themes/Sizes';
import styles from './NavBarFirstFragmentStyles';
import dropdownIcon from '../../../assets/icons/dropdown_icon.svg';
import logo from '../../../assets/icons/logo.svg';

/**
 * NavBarFirstFragment renders:
 * - a tranparent button which toggles the sidebar
 * - a dropdown icon uderneath the button
 * - a logo icon
 * - when the width of the viewport becomes too small, the text inside the button becomes invisible and the icon takes up the space
 * Props:
 * - dimensions: object ( contains the current height and width of the viewport )
 * - toggleGradeSidebar: function ( toggles the sidebar )
 * - dropdownButtonRef: object ( ref for the button )
 */
export default function NavBarFirstFragment(props) {
  // Props
  const dimensions = props.dimensions;

  return (
    <Nav style={styles.navBarFirstFragment}>
      <button
        style={styles.toggleMaterialsButton(dimensions.height)}
        onClick={props.toggleGradeSidebar}
        ref={props.dropdownButtonRef}
      >
        <span
          style={{
            ...fontStyle.body_semibold,
            fontSize: '16px',
            visibility:
              (gradeSidebarButtonWidth * dimensions.width) / 100 >=
              minMaterialsButtonWidth
                ? 'visible'
                : 'hidden',
          }}
        >
          Materiale
        </span>
      </button>
      <Container style={styles.dropdownIconContainer}>
        <img
          style={
            (gradeSidebarButtonWidth * dimensions.width) / 100 >=
            minMaterialsButtonWidth
              ? styles.dropDownIcon(dimensions.height)
              : styles.dropDownIconSingle(dimensions.height)
          }
          src={dropdownIcon}
          onClick={() => {
            props.toggleGradeSidebar;
          }}
          alt="dropDown"
        />
      </Container>
      <img style={styles.logo(dimensions.height)} src={logo} alt="logo" />
    </Nav>
  );
}
