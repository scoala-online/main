import { minNavBarHeight, navBarHeight } from '../../themes/Sizes';

const layoutStyle = {
  contentStyle: (windowHeight) => ({
    width: '100vw !important',
    position: 'fixed',
    top:
      (navBarHeight * windowHeight) / 100 >= minNavBarHeight
        ? navBarHeight.toString() + 'vh'
        : minNavBarHeight.toString() + 'px',
    bottom: '0px',
    margin: '0px',
    padding: '0px',
    zIndex: '10',
    overflowY: 'auto',
  }),
};

export default layoutStyle;
