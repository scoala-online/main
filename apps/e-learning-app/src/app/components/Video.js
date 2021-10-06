import { Link } from 'react-router-dom'
const Video = (props) => {
    const {video} = props
    const { thumbnail, title, videoLink, channel, length } = video
    const video_style = {
        fontFamily: "Open Sans, sans-serif",
        padding: "5px 20px 5px 0",
        minWidth: "300px",
        maxWidth: "300px",
        minHeight: "300px",
        maxHeight: "300px",
    };
    const title_style = {
        fontSize: "20px",
        overflowWrap: "break-word",
        margin: "10px 0",
    };
    const title_link_style = {
        color: "#000",
        textDecoration: "none",
    };
    const channel_style = {
        fontSize: "16px",
        marginBottom: "5px",
        paddingRight: "0%",
        textAlign: "right",
        overflowWrap: "break-word",
    };
    const channel_link_style = {
        color: "rgb(0,101,101)",
        textDecoration: "none",
    };
    const thumbnail_style = {
        position: "relative",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        overflow: "hidden",
    };
    const length_style = {
        position: "absolute",
        background: "black",
        color: "white",
        fontSize: "12px",
        padding: "5px 5px 5px 5px",
        bottom: "3px",
        left: "0px",
    };

    const thumbnail_link_style = {
        minWidth: "100%",
        minHeight: "100%",
        borderRadius: "2px",
    }

    const img_style = {
        minWidth: "100%",
        minHeight: "100%",
        borderRadius: "2px",
    }
    const getLengthString = length => {
        // 208 -> 03:28
        var sec_num = parseInt(length)
        var hours = Math.floor(sec_num / 3600)
        var minutes = Math.floor(sec_num / 60) % 60
        var seconds = sec_num % 60

        return [hours, minutes, seconds] // 0, 3, 28
            .map(v => v < 10 ? "0" + v : v) // 00, 03, 28
            .filter((v, i) => v !== "00" || i > 0) // 03, 28
            .join(":") // 03:28
    }
    return (
        <div style={video_style}>
            <div style={thumbnail_style}>
                <Link style={thumbnail_link_style} to={channel.link}>
                    <img style={img_style} href={channel.link} src={thumbnail} />
                </Link>
                <span style={length_style}>{getLengthString(length)}</span>
            </div>
            <h4 style={title_style}>
                <Link style={title_link_style} to={channel.link}>
                    {title}
                </Link>
            </h4>
            <p style={channel_style}>
                <Link style={channel_link_style} to={channel.link}>
                    {channel.name}
                </Link>
            </p>
        </div>
    )
}

export default Video
