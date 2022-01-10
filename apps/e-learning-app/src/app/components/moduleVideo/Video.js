import { Link } from 'react-router-dom';
import style from './VideoStyle';

/**
 * MODULE PAGE
 *
 * Video renders:
 * - a photo
 * - two paragraphs under the photo
 * - component (showing numbers) placed down-left of the photo
 *
 * Props:
 * - thumbnail: string (the path of the photo)
 * - title: string (the name displayed under the photo)
 * - videoLink: string (the link of the video's coresponding page)
 * - channel: string (the link to the user that created the video)
 * - length: int (number of seconds the video takes)
 *
 */

const Video = (props) => {
  const { video } = props;
  const { thumbnail, title, videoLink, channel, length } = video;

  const getLengthString = (length) => {
    // 208 -> 03:28
    var sec_num = parseInt(length);
    var hours = Math.floor(sec_num / 3600);
    var minutes = Math.floor(sec_num / 60) % 60;
    var seconds = sec_num % 60;

    return [hours, minutes, seconds] // 0, 3, 28
      .map((v) => (v < 10 ? '0' + v : v)) // 00, 03, 28
      .filter((v, i) => v !== '00' || i > 0) // 03, 28
      .join(':'); // 03:28
  };
  return (
    <div style={style.video_style}>
      <div style={style.thumbnail_style}>
        <Link style={style.thumbnail_link_style} to={channel.link}>
          <img
            style={style.img_style}
            href={channel.link}
            src={thumbnail}
            alt={title}
          />
        </Link>
        <span style={style.length_style}>{getLengthString(length)}</span>
      </div>
      <h4 style={style.title_style}>
        <Link style={style.title_link_style} to={channel.link}>
          {title}
        </Link>
      </h4>
      <p style={style.channel_style}>
        <Link style={style.channel_link_style} to={channel.link}>
          {channel.name}
        </Link>
      </p>
    </div>
  );
};

export default Video;
