import {
  navBarHeight,
  sidebarButtonHeight,
  minNavBarHeight,
  minSidebarButtonHeight,
  materialsButtonTop,
  minMaterialsButtonTop,
  dropdownIconTop,
  minDropdownIconTop,
  materialsButtonHeight,
  minMaterialsButtonHeight,
  minDropdownIconHeight,
  dropdownIconHeight,
  gradeSidebarButtonWidth,
  minMaterialsButtonWidth,
  logoHeight,
  minLogoHeight,
  logoTop,
  minLogoTop,
} from '../../themes/Sizes';

const navMenuStyles = {
  navBar: {
    filter: 'drop-shadow(0px 0.5px 8px rgba(0, 0, 0, 0.25))',
    width: '100vw',
    position: 'fixed',
    backgroundColor: '#fff',
    height: `${navBarHeight}vh`,
    minHeight: `${minNavBarHeight}px`,
    zIndex: '99',
  },
};

export default navMenuStyles;
