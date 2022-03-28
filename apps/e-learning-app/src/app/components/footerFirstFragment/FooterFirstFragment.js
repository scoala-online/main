import fontStyle from '../../themes/FontFamilies';
import styles from './FooterFirstFragmentStyles';
import emailIcon from '../../../assets/icons/email_icon.svg'
import facebookIcon from '../../../assets/icons/facebook_icon.svg'
import githubIcon from '../../../assets/icons/github_icon.svg'
import linkedinIcon from '../../../assets/icons/linkedin_icon.svg'
import React from 'react';

export default function FooterFirstFragment(props) {
    const dimensions = props.dimensions;

    return (
        <div style={styles.FooterFirstFragment}>
            <span>Contact</span>
            <span>Vrei sa ne oferi feedback? Sau ne poti ajuta cu ceva? Da-ne un semn!</span>
            <img 
              style={styles.emailIcon(dimensions.height)}
              src={emailIcon}
              alt="email icon" 
            />
            <a href="mailto: contact@scoalaonline.com">contact@scoalaonline.com</a>
            <span>Social media: </span>
            <img 
              style={styles.facebookIcon(dimensions.height)}
              src={facebookIcon}
              alt="facebook" 
            />
            <img 
              style={styles.githubIcon(dimensions.height)}
              src={githubIcon}
              alt="github" 
            />
            <img 
              style={styles.linkedinIcon(dimensions.height)}
              src={linkedinIcon}
              alt="linkedin" 
            />
        </div>
    )
}