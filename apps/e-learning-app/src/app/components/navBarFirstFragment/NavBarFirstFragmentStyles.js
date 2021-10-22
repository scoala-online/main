import { navBarHeight, minNavBarHeight, materialsButtonTop, minMaterialsButtonTop, dropdownIconTop, minDropdownIconTop, materialsButtonHeight, minMaterialsButtonHeight, minDropdownIconHeight, dropdownIconHeight, gradeSidebarButtonWidth, logoHeight, minLogoHeight, logoTop, minLogoTop } from "../../themes/Sizes";

const navBarFirstFragmentStyles = {
  navBarFirstFragment: {
    width: '60vw',
    height: `${navBarHeight}vh`,
    minHeight: `${minNavBarHeight}px`,
  },

  toggleMaterialsButton : (windowHeight) => ({
    background: 'transparent',
    margin: '0px',
    padding: '0px',
    position: 'fixed',
    width: gradeSidebarButtonWidth.toString() + 'vw',
    height: navBarHeight * windowHeight / 100 >= minNavBarHeight ? `${materialsButtonHeight}vh` : `${minMaterialsButtonHeight}px`,
    top: navBarHeight * windowHeight / 100 >= minNavBarHeight ? `${materialsButtonTop}vh` : `${minMaterialsButtonTop}px`,
    zIndex: '90',
    border: '0px',
  }),

  dropdownIconContainer: {
    display:'flex',
    justifyContent:'center',
    width:'15.8vw',
    margin: '0',
  },

  dropDownIcon: (windowHeight) => ({
    position: 'fixed',
    width: '20px',
    height: navBarHeight * windowHeight / 100 >= minNavBarHeight ? `${dropdownIconHeight}vh` : `${minDropdownIconHeight}px`,
    top: navBarHeight * windowHeight / 100 >= minNavBarHeight ? `${dropdownIconTop}vh` : `${minDropdownIconTop}px`,
    zIndex: '89',
  }),

  dropDownIconSingle: (windowHeight) => ({
    position: 'fixed',
    width: navBarHeight * windowHeight / 100 >= minNavBarHeight ? `${navBarHeight}vh` : `${minNavBarHeight}px`,
    height: navBarHeight * windowHeight / 100 >= minNavBarHeight ? `${navBarHeight}vh` : `${minNavBarHeight}px`,
    zIndex: '89',
  }),

  logo: (windowHeight) => ({
    position: 'fixed',
    width: navBarHeight * windowHeight / 100 >= minNavBarHeight ? `${logoHeight}vh` : `${minLogoHeight}px`,
    height: navBarHeight * windowHeight / 100 >= minNavBarHeight ? `${logoHeight}vh` : `${minLogoHeight}px`,
    top:  navBarHeight * windowHeight / 100 >= minNavBarHeight ? `${logoTop}vh` : `${minLogoTop}px`,
    //left: '47.6vw',
    right: '45vw'
  }),

};

export default navBarFirstFragmentStyles;
