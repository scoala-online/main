import { useRef, useState, useEffect } from 'react';
import { Navbar } from 'react-bootstrap';
import { useHistory } from 'react-router-dom';
import NavBarFirstFragment from '../navBarFirstFragment/NavBarFirstFragment';
import NavBarSecondFragment from '../navBarSecondFragment/NavBarSecondFragment';

import Sidebar from '../sidebar/SideBar';
import {
  navBarHeight,
  gradeSidebarButtonWidth,
  minMaterialsButtonWidth,
} from '../../themes/Sizes';

import styles from './NavMenuStyles';

/**
 * NavMenu renders:
 * - a navbar at the top of the page containing the NavBarFirstFragment and NavBarSecondFragment components
 * - two sidebars made of the SideBar component
 *
 * Props:
 * dimensions: object ( contains the current height and width of the viewport )
 *
 * State:
 * - gradeActive: boolean ( the grade sidebar's visibility )
 * - subjectActive: boolean ( the subject sidebar's visibility )
 * - subjectPos: number ( position of the first element of the subject sidebar )
 * - searchModalActive: boolean ( the search modal's visibility )
 */
export default function NavMenu(props) {
  // Constants
  const history = useHistory();

  // Props
  const dimensions = props.dimensions;

  // States
  const [gradeActive, setGradeActive] = useState(false);
  const [subjectActive, setSubjectActive] = useState(false);
  const [subjectPos, setSubjectPos] = useState(0);
  const [searchModalActive, setSearchModalActive] = useState(false);
  const [currentGrade, setCurrentGrade] = useState("");

  // Refs
  const dropdownButtonRef = useRef();
  const gradeSidebarRef = useRef();
  const subjectSidebarRef = useRef();

  /**
   * Toggles the subject sidebar.
   * @param {Number} index - Position of the first element of the sidebar.
   */
  function toggleSubjectSidebar(index, item) {
    if (index === subjectPos) {
      setSubjectActive(!subjectActive);
    } else {
      setSubjectPos(index);
      setCurrentGrade(item);
    }
  }

  /**
   * Toggles the grade sidebar.
   */
  function toggleGradeSidebar() {
    setGradeActive(!gradeActive);
  }

  /**
   * Toggles the search modal.
   */
  function toggleSearchModal() {
    setSearchModalActive(!searchModalActive);
  }

  // TODO: implement function when the page is ready.
  function goToPage(index, item) {
    history.push(`/index/${currentGrade.value}/${item.value}`);
  }

  /**
   * Closes the sidebars when the user clicks outside of them.
   */
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

  /**
   * Activates the subject sidebar when subjectPos state is changed.
   */
  useEffect(() => {
    if (!subjectActive) setSubjectActive(true);
    // Dependency not needed
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [subjectPos]);

  /**
   * Disables the subject sidebar if the grade sidebar is disabled.
   */
  useEffect(() => {
    if (!gradeActive) {
      setSubjectActive(false);
    }
  }, [gradeActive]);

  /**
   * Disables the search modal if the width of the viewport is big enough to fit the search input field inside the navbar.
   */
  useEffect(() => {
    if (
      (gradeSidebarButtonWidth * props.dimensions.width) / 100 >=
      minMaterialsButtonWidth
    ) {
      setSearchModalActive(false);
    }
  }, [dimensions]);

  return (
    <>
      <Navbar
        style={{ ...styles.navBar, height: navBarHeight.toString() + 'vh' }}
      >
        <NavBarFirstFragment
          dimensions={dimensions}
          toggleGradeSidebar={toggleGradeSidebar}
          dropdownButtonRef={dropdownButtonRef}
        />

        <NavBarSecondFragment
          dimensions={dimensions}
          searchModalActive={searchModalActive}
          toggleSearchModal={toggleSearchModal}
        />
      </Navbar>

      {
        <div style={{ overflow: 'hidden' }}>
          <Sidebar
            type="grade"
            active={gradeActive}
            sideBarRef={gradeSidebarRef}
            onClickFunction={toggleSubjectSidebar}
            dimensions={dimensions}
            fontSize="1rem"
          />

          <Sidebar
            type="subject"
            subjectPos={subjectPos}
            active={subjectActive}
            sideBarRef={subjectSidebarRef}
            onClickFunction={goToPage}
            dimensions={dimensions}
            fontSize="0.75rem"
          />
        </div>
      }
    </>
  );
}
