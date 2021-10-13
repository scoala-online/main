import React, {useRef, useState, useEffect} from "react";
import { Nav, Navbar } from "react-bootstrap";

import Sidebar from "./SideBar";

import { navMenuSize } from "../utilities/UtilityValues";
import { clase, materii } from "../utilities/Data";

import * as styles from "../styles/NavMenuStyles";
import dropdownIcon from "../../assets/icons/dropdown_icon.svg";
import logo from "../../assets/icons/logo.svg";

export default function NavMenu(props) {
    //States
    const [gradeActive, setGradeActive] = useState(false);
    const [subjectActive, setSubjectActive] = useState(false);
    const [posSubject, setPosSubject] = useState(0);

    //Refs
    const dropdownButtonRef = useRef();
    const gradeSidebarRef = useRef();
    const subjectSidebarRef = useRef();

    //Show/hide second sidebar
    function showSubjectActive(index){
        console.log(index);
        if (index != posSubject) {
            setPosSubject(index);
        } else {
            setSubjectActive(!subjectActive);
        }
    };

    //To Do
    function goToPage (index) {
        console.log(index);
    }

    //Used for closing the sidebars when the user clicks outside them
    useEffect(() => {
        const checkIfClickedOutside = e => {
          if (gradeActive 
            && dropdownButtonRef.current && !dropdownButtonRef.current.contains(e.target)
            && gradeSidebarRef.current && !gradeSidebarRef.current.contains(e.target)
            && subjectSidebarRef.current && !subjectSidebarRef.current.contains(e.target)) {
            setGradeActive(false);
          }
        }
    
        document.addEventListener("mousedown", checkIfClickedOutside)
    
        return () => {
          document.removeEventListener("mousedown", checkIfClickedOutside)
        }
    }, [gradeActive, subjectActive])

    useEffect(() => {
        if (!subjectActive)
            setSubjectActive(!subjectActive);
    },[posSubject])

    useEffect(() => {
        if (!gradeActive) {
            setSubjectActive(false);
        }
    }, [gradeActive])

    return (
        <>
            <Navbar
                style={{...styles.navBar, height:navMenuSize.toString()+'vh'}}
            >
                <Nav style={styles.navBarFirstFragment}>
                    <button 
                        style={styles.showMaterialsButton}
                        onClick={() => setGradeActive(!gradeActive)}
                        ref={dropdownButtonRef}
                    >
                        <span
                            style={{...styles.txt_600, fontSize:"1rem"}}
                        >
                            Materiale
                        </span>
                    </button>
                    <img style={styles.dropDownIcon} src={dropdownIcon} />
                    <img style={styles.logo} src={logo} />
                </Nav>

                <Nav style={styles.navBarSecondFragment}>
                    <input
                        style={styles.searchInputField}
                        type="text"
                        placeholder="search bar" />   
                    <button
                        style={styles.showAuthenticationButton}
                    >
                        <span
                            style={{...styles.txt_600, fontSize:"1rem", color:"#fff"}}
                        >
                            Autentificare
                        </span>
                    </button>
                </Nav>
            </Navbar>

            {<div style={{overflow:"hidden"}}>
                <Sidebar
                    type="grade"
                    data={clase}
                    posSubject={posSubject}
                    active={gradeActive}
                    sideBarRef={gradeSidebarRef}
                    onClickFunction={showSubjectActive}
                    fontSize="1rem"
                />

                <Sidebar
                    type="subject"
                    data={materii}
                    posSubject={posSubject}
                    active={subjectActive}
                    sideBarRef={subjectSidebarRef}
                    onClickFunction={goToPage}
                    fontSize="0.75rem"
                />
            </div>}
        </>
    );
}