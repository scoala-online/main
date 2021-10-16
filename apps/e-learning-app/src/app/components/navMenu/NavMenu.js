import { useRef, useState, useEffect } from 'react';
import { Nav, Navbar } from 'react-bootstrap';

import Sidebar from '../sidebar/SideBar';
import { navBarHeight } from '../../themes/Sizes';
import fontStyle from '../../themes/FontFamilies';
import { clase, materii } from '../../utilities/MockData';

import styles from './NavMenuStyles';
import dropdownIcon from '../../../assets/icons/dropdown_icon.svg';
import logo from '../../../assets/icons/logo.svg';

export default function NavMenu(props) {
  // States
  const [gradeActive, setGradeActive] = useState(false);
  const [subjectActive, setSubjectActive] = useState(false);
  const [subjectPos, setSubjectPos] = useState(0);

  // Refs
  const dropdownButtonRef = useRef();
  const gradeSidebarRef = useRef();
  const subjectSidebarRef = useRef();

  // Toggle second sidebar
  function showSubjectSidebar(index) {
    if (index !== subjectPos) {
      setSubjectPos(index);
    } else {
      setSubjectActive(!subjectActive);
    }
  }

  // TODO: implement function when the page is ready
  function goToPage(index) {
    return;
  }

  // Used for closing the sidebars when the user clicks outside of them
  useEffect(() => {
    const checkIfClickedOutside = (e) => {
      if (
        gradeActive &&
        dropdownButtonRef.current &&
        !dropdownButtonRef.current.contains(e.target) &&
        gradeSidebarRef.current &&
        !gradeSidebarRef.current.contains(e.target) &&
        subjectSidebarRef.current &&
        !subjectSidebarRef.current.contains(e.target)
      ) {
        setGradeActive(false);
      }
    };

    document.addEventListener('mousedown', checkIfClickedOutside);

    return () => {
      document.removeEventListener('mousedown', checkIfClickedOutside);
    };
  }, [gradeActive, subjectActive]);

  useEffect(() => {
    if (!subjectActive) setSubjectActive(!subjectActive);
  // Dependency not needed
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [subjectPos]);

  useEffect(() => {
    if (!gradeActive) {
      setSubjectActive(false);
    }
  }, [gradeActive]);

  return (
    <>
      <Navbar
        style={{ ...styles.navBar, height: navBarHeight.toString() + 'vh' }}
      > 
        <Nav style={styles.navBarFirstFragment}>
          <button
            style={styles.showMaterialsButton}
            onClick={() => setGradeActive(!gradeActive)}
            ref={dropdownButtonRef}
          >
            <span style={{ ...fontStyle.body_semibold, fontSize: '1rem' }}>
              Materiale
            </span>
          </button>
          <img style={styles.dropDownIcon} src={dropdownIcon} alt='dropDown'/>
          <img style={styles.logo} src={logo} alt='logo'/>
        </Nav>

        <Nav style={styles.navBarSecondFragment}>
          <input
            style={styles.searchInputField}
            type="text"
            placeholder="search bar"
          />
          <button style={styles.showAuthenticationButton}>
            <span
              style={{ ...fontStyle.body_semibold, fontSize: '1rem', color: '#fff' }}
            >
              Autentificare
            </span>
          </button>
        </Nav>
      </Navbar>

      {
        <div style={{ overflow: 'hidden' }}>
          <Sidebar
            type="grade"
            data={clase}
            active={gradeActive}
            sideBarRef={gradeSidebarRef}
            onClickFunction={showSubjectSidebar}
            fontSize="1rem"
          />

          <Sidebar
            type="subject"
            data={materii}
            posSubject={subjectPos}
            active={subjectActive}
            sideBarRef={subjectSidebarRef}
            onClickFunction={goToPage}
            fontSize="0.75rem"
          />
        </div>
      }
    </>
  );
}
