package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.PostgresDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Pcs.schema ++ Profiles.schema ++ Reviews.schema ++ Submissions.schema ++ Users.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Pcs
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param owner Database column owner SqlType(int4)
   *  @param member Database column member SqlType(int4) */
  case class PcsRow(id: Int, owner: Int, member: Int)
  /** GetResult implicit for fetching PcsRow objects using plain SQL queries */
  implicit def GetResultPcsRow(implicit e0: GR[Int]): GR[PcsRow] = GR{
    prs => import prs._
    PcsRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table pcs. Objects of this class serve as prototypes for rows in queries. */
  class Pcs(_tableTag: Tag) extends Table[PcsRow](_tableTag, "pcs") {
    def * = (id, owner, member) <> (PcsRow.tupled, PcsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(owner), Rep.Some(member)).shaped.<>({r=>import r._; _1.map(_=> PcsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column owner SqlType(int4) */
    val owner: Rep[Int] = column[Int]("owner")
    /** Database column member SqlType(int4) */
    val member: Rep[Int] = column[Int]("member")
  }
  /** Collection-like TableQuery object for table Pcs */
  lazy val Pcs = new TableQuery(tag => new Pcs(tag))

  /** Entity class storing rows of table Profiles
   *  @param id Database column id SqlType(serial), AutoInc
   *  @param user Database column user SqlType(int4)
   *  @param name Database column name SqlType(text), Default(None)
   *  @param description Database column description SqlType(text), Default(None)
   *  @param field Database column field SqlType(text), Default(None)
   *  @param concentration Database column concentration SqlType(text), Default(None)
   *  @param sex Database column sex SqlType(text), Default(None)
   *  @param location Database column location SqlType(text), Default(None)
   *  @param academicrank Database column academicrank SqlType(text), Default(None)
   *  @param thesistitle Database column thesistitle SqlType(text), Default(None)
   *  @param thesisadvisor Database column thesisadvisor SqlType(text), Default(None)
   *  @param publications Database column publications SqlType(text), Default(None)
   *  @param aboutyou Database column aboutyou SqlType(text), Default(None) */
  case class ProfilesRow(id: Int, user: Int, name: Option[String] = None, description: Option[String] = None, field: Option[String] = None, concentration: Option[String] = None, sex: Option[String] = None, location: Option[String] = None, academicrank: Option[String] = None, thesistitle: Option[String] = None, thesisadvisor: Option[String] = None, publications: Option[String] = None, aboutyou: Option[String] = None)
  /** GetResult implicit for fetching ProfilesRow objects using plain SQL queries */
  implicit def GetResultProfilesRow(implicit e0: GR[Int], e1: GR[Option[String]]): GR[ProfilesRow] = GR{
    prs => import prs._
    ProfilesRow.tupled((<<[Int], <<[Int], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table profiles. Objects of this class serve as prototypes for rows in queries. */
  class Profiles(_tableTag: Tag) extends Table[ProfilesRow](_tableTag, "profiles") {
    def * = (id, user, name, description, field, concentration, sex, location, academicrank, thesistitle, thesisadvisor, publications, aboutyou) <> (ProfilesRow.tupled, ProfilesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(user), name, description, field, concentration, sex, location, academicrank, thesistitle, thesisadvisor, publications, aboutyou).shaped.<>({r=>import r._; _1.map(_=> ProfilesRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column user SqlType(int4) */
    val user: Rep[Int] = column[Int]("user")
    /** Database column name SqlType(text), Default(None) */
    val name: Rep[Option[String]] = column[Option[String]]("name", O.Default(None))
    /** Database column description SqlType(text), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))
    /** Database column field SqlType(text), Default(None) */
    val field: Rep[Option[String]] = column[Option[String]]("field", O.Default(None))
    /** Database column concentration SqlType(text), Default(None) */
    val concentration: Rep[Option[String]] = column[Option[String]]("concentration", O.Default(None))
    /** Database column sex SqlType(text), Default(None) */
    val sex: Rep[Option[String]] = column[Option[String]]("sex", O.Default(None))
    /** Database column location SqlType(text), Default(None) */
    val location: Rep[Option[String]] = column[Option[String]]("location", O.Default(None))
    /** Database column academicrank SqlType(text), Default(None) */
    val academicrank: Rep[Option[String]] = column[Option[String]]("academicrank", O.Default(None))
    /** Database column thesistitle SqlType(text), Default(None) */
    val thesistitle: Rep[Option[String]] = column[Option[String]]("thesistitle", O.Default(None))
    /** Database column thesisadvisor SqlType(text), Default(None) */
    val thesisadvisor: Rep[Option[String]] = column[Option[String]]("thesisadvisor", O.Default(None))
    /** Database column publications SqlType(text), Default(None) */
    val publications: Rep[Option[String]] = column[Option[String]]("publications", O.Default(None))
    /** Database column aboutyou SqlType(text), Default(None) */
    val aboutyou: Rep[Option[String]] = column[Option[String]]("aboutyou", O.Default(None))

    /** Uniqueness Index over (id) (database name profiles_id_uindex) */
    val index1 = index("profiles_id_uindex", id, unique=true)
    /** Uniqueness Index over (user) (database name profiles_user_uindex) */
    val index2 = index("profiles_user_uindex", user, unique=true)
  }
  /** Collection-like TableQuery object for table Profiles */
  lazy val Profiles = new TableQuery(tag => new Profiles(tag))

  /** Entity class storing rows of table Reviews
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param submission Database column submission SqlType(int4)
   *  @param reviewer Database column reviewer SqlType(int4)
   *  @param contents Database column contents SqlType(text)
   *  @param status Database column status SqlType(text) */
  case class ReviewsRow(id: Int, submission: Int, reviewer: Int, contents: String, status: String)
  /** GetResult implicit for fetching ReviewsRow objects using plain SQL queries */
  implicit def GetResultReviewsRow(implicit e0: GR[Int], e1: GR[String]): GR[ReviewsRow] = GR{
    prs => import prs._
    ReviewsRow.tupled((<<[Int], <<[Int], <<[Int], <<[String], <<[String]))
  }
  /** Table description of table reviews. Objects of this class serve as prototypes for rows in queries. */
  class Reviews(_tableTag: Tag) extends Table[ReviewsRow](_tableTag, "reviews") {
    def * = (id, submission, reviewer, contents, status) <> (ReviewsRow.tupled, ReviewsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(submission), Rep.Some(reviewer), Rep.Some(contents), Rep.Some(status)).shaped.<>({r=>import r._; _1.map(_=> ReviewsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column submission SqlType(int4) */
    val submission: Rep[Int] = column[Int]("submission")
    /** Database column reviewer SqlType(int4) */
    val reviewer: Rep[Int] = column[Int]("reviewer")
    /** Database column contents SqlType(text) */
    val contents: Rep[String] = column[String]("contents")
    /** Database column status SqlType(text) */
    val status: Rep[String] = column[String]("status")
  }
  /** Collection-like TableQuery object for table Reviews */
  lazy val Reviews = new TableQuery(tag => new Reviews(tag))

  /** Entity class storing rows of table Submissions
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param to Database column to SqlType(int4)
   *  @param from Database column from SqlType(int4)
   *  @param status Database column status SqlType(text)
   *  @param contents Database column contents SqlType(text) */
  case class SubmissionsRow(id: Int, to: Int, from: Int, status: String, contents: String)
  /** GetResult implicit for fetching SubmissionsRow objects using plain SQL queries */
  implicit def GetResultSubmissionsRow(implicit e0: GR[Int], e1: GR[String]): GR[SubmissionsRow] = GR{
    prs => import prs._
    SubmissionsRow.tupled((<<[Int], <<[Int], <<[Int], <<[String], <<[String]))
  }
  /** Table description of table submissions. Objects of this class serve as prototypes for rows in queries. */
  class Submissions(_tableTag: Tag) extends Table[SubmissionsRow](_tableTag, "submissions") {
    def * = (id, to, from, status, contents) <> (SubmissionsRow.tupled, SubmissionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(to), Rep.Some(from), Rep.Some(status), Rep.Some(contents)).shaped.<>({r=>import r._; _1.map(_=> SubmissionsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column to SqlType(int4) */
    val to: Rep[Int] = column[Int]("to")
    /** Database column from SqlType(int4) */
    val from: Rep[Int] = column[Int]("from")
    /** Database column status SqlType(text) */
    val status: Rep[String] = column[String]("status")
    /** Database column contents SqlType(text) */
    val contents: Rep[String] = column[String]("contents")
  }
  /** Collection-like TableQuery object for table Submissions */
  lazy val Submissions = new TableQuery(tag => new Submissions(tag))

  /** Entity class storing rows of table Users
   *  @param email Database column email SqlType(text)
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param passwordhash Database column passwordhash SqlType(text)
   *  @param passwordsalt Database column passwordsalt SqlType(text)
   *  @param passworditerations Database column passworditerations SqlType(int4) */
  case class UsersRow(email: String, id: Int, passwordhash: String, passwordsalt: String, passworditerations: Int)
  /** GetResult implicit for fetching UsersRow objects using plain SQL queries */
  implicit def GetResultUsersRow(implicit e0: GR[String], e1: GR[Int]): GR[UsersRow] = GR{
    prs => import prs._
    UsersRow.tupled((<<[String], <<[Int], <<[String], <<[String], <<[Int]))
  }
  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends Table[UsersRow](_tableTag, "users") {
    def * = (email, id, passwordhash, passwordsalt, passworditerations) <> (UsersRow.tupled, UsersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(email), Rep.Some(id), Rep.Some(passwordhash), Rep.Some(passwordsalt), Rep.Some(passworditerations)).shaped.<>({r=>import r._; _1.map(_=> UsersRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column email SqlType(text) */
    val email: Rep[String] = column[String]("email")
    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column passwordhash SqlType(text) */
    val passwordhash: Rep[String] = column[String]("passwordhash")
    /** Database column passwordsalt SqlType(text) */
    val passwordsalt: Rep[String] = column[String]("passwordsalt")
    /** Database column passworditerations SqlType(int4) */
    val passworditerations: Rep[Int] = column[Int]("passworditerations")

    /** Uniqueness Index over (email) (database name users_email_uindex) */
    val index1 = index("users_email_uindex", email, unique=true)
  }
  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
}
