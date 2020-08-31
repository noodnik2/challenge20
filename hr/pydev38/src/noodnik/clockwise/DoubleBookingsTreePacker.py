
debug_enabled = False

def packed_meetings(meetings_list):

    # construct a set of meeting tuples, each sorted by attendee id
    ordered_meeting_set = set( tuple(sorted(meeting)) for meeting in meetings_list )

    tree = None
    for meeting in ordered_meeting_set:
        tree = add_meeting(tree, meeting)

    return []

#
#   insertion of a meeting into the tree:
#   - on the right are non-conflicts
#   - on the left are conflicts
#
#
#

def add_meeting(tree, meeting):
    if tree == None:
        node = Node()
        node.data = meeting
        return node
    if has_double_booked(tree.data, meeting):
        node = Node()
        node.left = tree
        node.data = meeting
        return node
    node = Node()
    node.right = tree
    node.data = meeting
    return node

def debug(message):
    if debug_enabled:
        print("     debug: " + message)
#
# class Node:
#     def __init__(self):
#         self.left = None
#         self.right = None
#         self.data = None
