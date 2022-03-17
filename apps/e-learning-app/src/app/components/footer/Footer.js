import FooterFirstFragment from "../footerFirstFragment/FooterFirstFragment";
import FooterSecondFragment from "../footerSecondFragment/FooterSecondFragment";

import styles from './FooterStyles'

export default function Footer(props) {
    const dimensions = props.dimensions

    return(
        <div style={styles.footer}>
            <FooterFirstFragment dimensions={dimensions} />
            <FooterSecondFragment dimensions={dimensions} />
        </div>
    );
}