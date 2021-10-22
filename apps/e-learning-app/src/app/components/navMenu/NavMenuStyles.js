import { navBarHeight, minNavBarHeight } from '../../themes/Sizes';

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
