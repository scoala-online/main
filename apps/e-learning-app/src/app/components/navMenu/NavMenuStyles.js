import { navBarHeight } from "../../themes/Sizes";

const navMenuStyles = {
  navBar: {
    filter: 'drop-shadow(0px 0.5px 8px rgba(0, 0, 0, 0.25))',
    width: '100vw',
    position: 'fixed',
    backgroundColor: '#fff',
  },

  navBarFirstFragment: {
    width: '60vw',
    height: `${navBarHeight}vh`,
  },

  navBarSecondFragment: {
    background:
      'linear-gradient(180deg, #1F4E5A 0%, #009C8E 99.99%, rgba(31, 78, 90, 0) 100%)',
    width: '40vw',
    height: `${navBarHeight}vh`,
  },

  showMaterialsButton: {
    background: 'transparent',
    margin: '0px',
    padding: '0px',
    position: 'fixed',
    width: '15.8vw',
    height: '8.8vh',
    top: '-1.6vh',
    zIndex: '12',
    border: '0px',
  },

  dropDownIcon: {
    position: 'fixed',
    width: '2.56vw',
    height: '3.42vh',
    left: '6.67vw',
    top: '3.05vh',
    zIndex: '11',
  },

  logo: {
    position: 'fixed',
    width: '4.85vw',
    height: '5.27vh',
    top: '0.83vh',
    left: '47.6vw',
  },

  searchInputField: {
    position: 'fixed',
    width: '21.85vw',
    height: '3.15vh',
    top: '1.85vh',
    left: '61.18vw',
  },

  showAuthenticationButton: {
    background: 'transparent',
    margin: '0px',
    padding: '0px',
    position: 'fixed',
    width: '16.92vw',
    height: `${navBarHeight}vh`,
    left: '83.07vw',
    border: '0px',
  },
};

export default navMenuStyles;