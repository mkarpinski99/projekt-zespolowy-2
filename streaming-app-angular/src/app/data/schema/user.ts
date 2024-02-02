export interface User {
  username: string,
  moderator: boolean,
  timeouted: boolean,
  banned: boolean,
  stream_secret_key: string,
  stream_title: string,
  avatar_path?: string,
  followed: boolean
}
