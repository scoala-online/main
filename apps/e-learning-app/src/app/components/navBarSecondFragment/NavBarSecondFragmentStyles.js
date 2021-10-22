import { navBarHeight, minNavBarHeight, searchInputFieldHeight, minSearchInputFieldHeight} from "../../themes/Sizes";

const navBarSecondFragmentStyles = {
  navBarSecondFragment: {
    background:
      'linear-gradient(180deg, #1F4E5A 0%, #009C8E 99.99%, rgba(31, 78, 90, 0) 100%)',
    display: 'flex',
    alignItems: 'center',
    width: '40vw',
    height: `${navBarHeight}vh`,
    minHeight: `${minNavBarHeight}px`,
  },

  searchModal: (display, windowHeight) => ({
    background:
      'linear-gradient(180deg, #1F4E5A 0%, #009C8E 99.99%, rgba(31, 78, 90, 0) 100%)',
    display: display ? 'flex' : 'none',
    alignItems: 'center',
    position: 'fixed',
    width: '40vw',
    height: `${navBarHeight}vh`,
    minHeight: `${minNavBarHeight}px`,
    top: navBarHeight * windowHeight / 100 >= minNavBarHeight ? `${navBarHeight}vh` : `${minNavBarHeight}px`,
    left: '60vw',
  }),

  searchInputField: (windowHeight, isModal) => ({
    width: isModal ? '32.8vw' : '19.65vw',
    height: navBarHeight * windowHeight / 100 >= minNavBarHeight ? `${searchInputFieldHeight}vh` : `${minSearchInputFieldHeight}px`,

    marginLeft: '1.2vw',
    borderWidth: '1px',
    borderStyle: 'solid',
    borderColor: 'black',
  }),

  searchIconBorder: (windowHeight) => ({
    background: '#fff',
    margin: '0',
    padding: '0',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',

    width: '2.2vh',
    minWidth: `${minSearchInputFieldHeight}px`,
    height: navBarHeight * windowHeight / 100 >= minNavBarHeight ? `${searchInputFieldHeight}vh` : `${minSearchInputFieldHeight}px`,

    borderWidth: '1px',
    borderLeft: '0',
    borderStyle: 'solid',
    borderColor: 'black',
  }),

  searchIcon: {
    fontSize : '0.9rem',
  },

  searchIconSingle: {
    position: 'fixed',
    fontSize : '1.5rem',
    left: '67vw',
    cursor: 'pointer',
  },

  toggleAuthenticationButton: {
    background: 'transparent',
    margin: '0px',
    padding: '0px',
    position: 'fixed',
    width: '16.92vw',
    height: `${navBarHeight}vh`,
    minHeight: `${minNavBarHeight}px`,
    left: '83.07vw',
    border: '0px',
  },

  loginIcon: {
    position: 'fixed',
    fontSize : '1.7rem',
    left: '85vw',
    cursor: 'pointer',
  },
};

export default navBarSecondFragmentStyles;
