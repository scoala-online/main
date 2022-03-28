import {
    footerFirstFragmentHeight,
    minFooterFirstFragmentHeight,
    emailHeight,
    emailWidth,
    minEmailHeight,
    minEmailWidth,
} from '../../themes/Sizes';

//TODO: Change dimensions for social media icons
const FooterFirstFragmentStyles = {
    FooterFirstFragment: {
        display: 'flex',
        flexDirection: 'column',
        width: '100vw',
        height: `${footerFirstFragmentHeight}vh`,
        minHeight: `${minFooterFirstFragmentHeight}px`,
        backgroundColor: '#BDBDBD',
    },

    emailIcon: (windowHeight) => ({
        height:
            (footerFirstFragmentHeight * windowHeight) / 100 >= minFooterFirstFragmentHeight
            ? `${emailHeight}vh`
            : `${minEmailHeight}px`
    }),

    facebookIcon: (windowHeight) => ({
        height:
            (footerFirstFragmentHeight * windowHeight) / 100 >= minFooterFirstFragmentHeight
            ? `${emailHeight}vh`
            : `${minEmailHeight}px`
    }),

    githubIcon: (windowHeight) => ({
        height:
            (footerFirstFragmentHeight * windowHeight) / 100 >= minFooterFirstFragmentHeight
            ? `${emailHeight}vh`
            : `${minEmailHeight}px`
    }),

    linkedinIcon: (windowHeight) => ({
        height:
            (footerFirstFragmentHeight * windowHeight) / 100 >= minFooterFirstFragmentHeight
            ? `${emailHeight}vh`
            : `${minEmailHeight}px`
    })
}

export default FooterFirstFragmentStyles;