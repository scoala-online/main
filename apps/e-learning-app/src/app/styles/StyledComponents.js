//Contains react components with style created using 'styled-components'

import styled from 'styled-components';

//Container for the content of a sidebar
export const SidebarContainer = styled.div`
    background: ${({ type }) => (type === "grade" ? 'linear-gradient(180deg, rgba(0, 201, 183, 0.9) 0%, rgba(0, 224, 206, 0.9) 100%)' : 'linear-gradient(319.97deg, #009C8E 7.3%, #00E0CE 100%)')}; 
    border-radius: 0px;
    
    display: flex;
    flex-direction: column;
    font-size: 1.5rem;

    position: fixed;
    width: ${({ type }) => (type === "grade" ? '15.8vw' : '12.2vw')};
    bottom: ${({ bottomVal }) => (bottomVal)};
    top: ${({ topVal }) => (topVal)};
    left: ${({ active, type }) => (type === "grade" ? (active ? '0' : '-15.8vw') : (active ? '15.8vw' : '-28vw'))};
    z-index: ${({ type }) => (type === "grade" ? '10' : '9')};

    transition: left ${({ type }) => (type === "grade" ? '350ms' : '1000ms')};

    direction: ${({ type }) => (type === "grade" ? 'rtl' : 'ltr')};
    overflow-y: auto;
    overflow-x: hidden;
`;

//Button inside a sidebar used to open another sidebar or another page
export const LinkButton = styled.button`
    background: transparent;
    min-height: 6.85vh;
    width: ${({ type }) => (type === "grade" ? '15.8vw' : '12.2vw')};
    

    display: flex;
    justify-content: center;
    align-items: center;

    border-top: 0px solid #FFFDF6;
    border-right: 1px solid #FFFDF6;
    border-left: 1px solid #FFFDF6;
    border-bottom: ${({ type, last }) => (last ? '0' : type === "grade" ? '1px' : '1px')} solid #FFFDF6;

    box-sizing: border-box;
`;